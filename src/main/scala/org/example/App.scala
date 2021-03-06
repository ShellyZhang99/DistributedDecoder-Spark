package org.example
import org.apache.hadoop
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs._
import org.apache.hadoop.fs.permission.{FsAction, FsPermission}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.util.CollectionAccumulator
import org.apache.spark.{SparkConf, SparkContext}

import java.io.{File, FileOutputStream}

/**
 * Hello world!
 *
 */
object App extends Serializable {
  def main(args: Array[String]): Unit= {
    if (args.length <= 1) {
      System.err.println("Usage: SparkWordCount <inputfile>")
      System.exit(1)
    }

    val conf = new SparkConf().setMaster("local").setAppName("SparkWordCount").setExecutorEnv("executor-memory", "2g")
    conf.set("fs.defaultFS", "hdfs://172.24.5.137:9000")
    val sc = new SparkContext(conf)
    //sc.addFile(args(0))

    //sc.addFile(args(1))
    //sc.addFile(args(2))
    val cfg = new Configuration();
    //cfg.set("fs.defaultFS", "hdfs://172.24.5.137:9000");
    cfg.addResource(new Path("core-site.xml"));
    val hdfs : FileSystem = FileSystem.get(cfg)
    val localFS : FileSystem = FileSystem.get(new Configuration());
    val status = hdfs.listStatus(new Path(args(3)))
    val filePermission = new FsPermission(
      FsAction.ALL, // user
      FsAction.ALL, // group
      FsAction.ALL);
    localFS.setPermission(new Path("testFile/"), filePermission)
    for (i <- 0 until status.length) {
      val inputStream = hdfs.open(status(i).getPath)
      val nameExcludeSource : String = status(i).getPath.getName.toString
      val localFile : String = "testFile/" + nameExcludeSource;
      localFS.create(new Path(localFile));
      val outputStream : FileOutputStream = new FileOutputStream(new File(localFile));

      val buffer = new Array[Byte](32000);

      var len = inputStream.read(buffer)
      while (len != -1) {
        outputStream.write(buffer, 0, len - 1)
        len = inputStream.read(buffer)
      }
      outputStream.flush()
      inputStream.close()
      outputStream.close()
    }

    //val allChildren : Array[File] = hdfs.listFiles(new Path(args(3)), false);
    //HDFSHelper.copyFolderToLocal(hdfs, args(3), "/");

    val acc = sc.collectionAccumulator[Excl_decoder]("decoders")
    val rdd=sc.textFile(args(0)).cache().flatMap(line=>line.split(" "));
    val decoder = new Decoder();
    val decoderBroadcast = sc.broadcast(decoder)
    //rdd.foreach(item => acc.add(decoder.add_decoder(item.toString, "decoderFile")));
    //decoder.add_decoder("perf.data-aux-idx2.bin", args(1));
    rdd.foreach(item => forEachAddDecoder(acc, decoderBroadcast, item.toString, args(1)));
    decoderBroadcast.value.add_parallel_decoders(acc.value);
    val rdd2 = sc.textFile(args(1)).cache().flatMap(line=>line.split(" "));
    //decoderBroadcast.value.decode("16", args(2))
    rdd2.foreach(item =>decoderBroadcast.value.decode(item.toString, args(2)));
    sc.stop()

  }

  def forEachAddDecoder(acc:CollectionAccumulator[Excl_decoder], decoderBroadcast: Broadcast[Decoder], item:String, args:String):Int={
    acc.add(decoderBroadcast.value.add_decoder(item, args));
    return 1;
  }



}
/*object App{
  def main(args: Array[String]): Unit= {
    if (args.length == 0) {
      System.err.println("Usage: SparkWordCount <inputfile>")
      System.exit(1)
    }
    //?????????????????????????????????????????????????????????
    //???.setMaster("local[2]")?????????????????????
    //?????????????????????????????????
    val conf = new SparkConf().setAppName("SparkWordCount").setMaster("local")
    val sc = new SparkContext(conf)

    //wordcount??????????????????????????????Spark?????????
    val rdd2=sc.textFile(args(0)).filter(line => line.contains("Spark"))
    //???????????????????????????????????????HDFS???
    rdd2.saveAsTextFile(args(1))
    sc.stop()

  }

}*/
