import de.haw.tweetspace.avro.CustomerTweet;
import org.apache.avro.Schema;
import org.apache.avro.mapred.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Example MapReduce job that "calculates" Friend reccomendations for users that upload their tweets to the platform.
 * Will scan the CustomerTweet avro events and output A Pair<Long,Long> which is the user-id associated with
 * the user id that he made the most replies for
 */
public class CalculateRecommendations extends Configured implements Tool {
    public static class ColorCountMapper extends AvroMapper<CustomerTweet, Pair<Long, Long>> {
        @Override
        public void map(CustomerTweet datum, AvroCollector<Pair<Long, Long>> collector, Reporter reporter) throws IOException {
            collector.collect(new Pair<>(datum.getId(), datum.getInReplyToUserId()));
        }
    }

    public static class ColorCountReducer extends AvroReducer<Long, Long, Pair<Long, Long>> {
        @Override
        public void reduce(Long key, Iterable<Long> values, AvroCollector<Pair<Long, Long>> collector, Reporter reporter) throws IOException {
            Map<Long, Integer> counts = new HashMap<>();
            for (Long repliedToUserId : values) {
                counts.putIfAbsent(repliedToUserId, 0);
                counts.put(repliedToUserId, counts.get(repliedToUserId) + 1);
            }
            Long mostRepliedTo = Collections.max(counts.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
            collector.collect(new Pair<>(key, mostRepliedTo));
        }
    }

    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: CalculateRecommendations <input path> <output path>");
            return -1;
        }

        JobConf conf = new JobConf(getConf(), CalculateRecommendations.class);
        conf.setJobName("caclulate_reccomendations");

        FileInputFormat.setInputPaths(conf, new Path(args[0]));
        FileOutputFormat.setOutputPath(conf, new Path(args[1]));

        AvroJob.setMapperClass(conf, ColorCountMapper.class);
        AvroJob.setReducerClass(conf, ColorCountReducer.class);

        // Note that AvroJob.setInputSchema and AvroJob.setOutputSchema set
        // relevant config options such as input/output format, map output
        // classes, and output key class.
        AvroJob.setInputSchema(conf, CustomerTweet.getClassSchema());
        AvroJob.setOutputSchema(conf, Pair.getPairSchema(Schema.create(Schema.Type.LONG),
                Schema.create(Schema.Type.LONG)));

        JobClient.runJob(conf);
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new CalculateRecommendations(), args);
        System.exit(res);
    }

}
