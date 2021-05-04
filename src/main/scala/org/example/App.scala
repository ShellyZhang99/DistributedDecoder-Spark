package org.example
import org.apache.hadoop
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs._
import org.apache.hadoop.fs.permission.{FsAction, FsPermission}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.util.CollectionAccumulator
import org.apache.spark.{SparkConf, SparkContext}

import java.io.{File, FileOutputStream, PrintWriter, StringWriter}

/**
 * Hello world!
 *
 */
object App extends Serializable {
  def main(args: Array[String]): Unit= {
    try {

      val conf = new SparkConf().setMaster("spark://114.212.84.19:7077")
      conf.set("fs.defaultFS", "hdfs://114.212.84.19:9000")
      val sc = new SparkContext(conf)
      //sc.addFile(args(0))

      //sc.addFile(args(1))
      //sc.addFile(args(2))
      val cfg = new Configuration();

      println("\nYes0\n")
      cfg.set("fs.defaultFS", "hdfs://master:9000");
      //cfg.addResource(new Path("core-site.xml"));
      println("\nYes1\n")
      val hdfs: FileSystem = FileSystem.get(cfg)
      println("\nYes2\n")
      val localFS: FileSystem = FileSystem.get(new Configuration());
      println("\nYes3\n")
      val a = sc.textFile("hdfs://master:9000/DistributedDecoderTest/Test0/inputFile.txt").toString()
      println(a)
      println("\nYes32\n")
      val status = hdfs.listFiles(new Path("hdfs://master:9000/DistributedDecoderTest/Test0/"), false)
      println("\nYes4\n")
      val filePermission = new FsPermission(FsAction.ALL, FsAction.ALL, FsAction.ALL);
      println("\nYes5\n")
      localFS.setPermission(new Path("/home/bigdataflow/DistributedDecoder/Test1/"), filePermission)
      println("\nYes6\n")
     while (status.hasNext) {
       val temp = status.next()
        val inputStream = hdfs.open(temp.getPath)
        val nameExcludeSource: String = temp.getPath.getName.toString
        val localFile: String = "/home/bigdataflow/DistributedDecoder/Test1/" + nameExcludeSource;
        localFS.create(new Path(localFile));
        val outputStream: FileOutputStream = new FileOutputStream(new File(localFile));

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
      println("\nYes7\n")
      //val allChildren : Array[File] = hdfs.listFiles(new Path(args(3)), false);
      //HDFSHelper.copyFolderToLocal(hdfs, args(3), "/");

      val acc = sc.collectionAccumulator[Excl_decoder]("decoders")
      println("\nYes8\n")
      val rdd = sc.textFile("hdfs://master:9000/DistributedDecoderTest/Test0/inputFile.txt").cache().flatMap(line => line.split(" "));
      println("\nYes9\n")
      val decoder = new Decoder();
      println("\nYes10\n")
      val decoderBroadcast = sc.broadcast(decoder)
      println("\nYes11\n")
      rdd.foreach(item => acc.add(decoderBroadcast.value.add_decoder(item, "hdfs://master:9000/DistributedDecoderTest/Test0/decoderFile.txt")))
      println("\nYes12\n")
      decoderBroadcast.value.add_parallel_decoders(acc.value);
      println("\nYes13\n")
      val rdd2 = sc.textFile("hdfs://master:9000/DistributedDecoderTest/Test0/decoderFile.txt").cache().flatMap(line => line.split(" "));
      println("\nYes14\n")
      //decoderBroadcast.value.decode("16", args(2))
      rdd2.foreach(item => decoderBroadcast.value.decode(item.toString, "hdfs://master:9000/DistributedDecoderTest/Test0/outputFile.txt"));
      println("\nYes15\n")
      sc.stop()

    }
    catch{
      case e : Throwable=>{val sw:StringWriter = new StringWriter()
        val pw:PrintWriter = new PrintWriter(sw)
        e.printStackTrace(pw)
        println("======>>printStackTraceStr Exception: " + e.getClass() + "\n==>" + e.printStackTrace() + e.getMessage())}
    }

  }

  def forEachAddDecoder(acc:CollectionAccumulator[Excl_decoder], decoderBroadcast: Broadcast[Decoder], item:String, args:String):Int={
    println("\nAdd decoder1\n");
    acc.add(decoderBroadcast.value.add_decoder(item, args));
    println("\nAdd decoder 8\n")
    return 1;
  }



}
/*object App{
  def main(args: Array[String]): Unit= {
    if (args.length == 0) {
      System.err.println("Usage: SparkWordCount <inputfile>")
      System.exit(1)
    }
    //以本地线程方式运行，可以指定线程个数，
    //如.setMaster("local[2]")，两个线程执行
    //下面给出的是单线程执行
    val conf = new SparkConf().setAppName("SparkWordCount").setMaster("local")
    val sc = new SparkContext(conf)

    //wordcount操作，计算文件中包含Spark的行数
    val rdd2=sc.textFile(args(0)).filter(line => line.contains("Spark"))
    //保存内容，在例子中是保存在HDFS上
    rdd2.saveAsTextFile(args(1))
    sc.stop()

  }

}*/
