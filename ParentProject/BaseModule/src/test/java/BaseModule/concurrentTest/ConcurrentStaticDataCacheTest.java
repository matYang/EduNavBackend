package BaseModule.concurrentTest;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.json.JSONObject;
import org.junit.Test;

import BaseModule.cache.StaticDataRamCache;
import BaseModule.common.DateUtility;
import BaseModule.staticDataService.StaticDataService;
import BaseModule.staticDataService.SystemDataInit;

public class ConcurrentStaticDataCacheTest {
	
	public class TestReadThread extends Thread {  
		private CountDownLatch threadsSignal;
		private int index;
		private ArrayList<JSONObject> dataS;
		private String id;
		
		public TestReadThread(CountDownLatch threadsSignal, int index, ArrayList<JSONObject> dataS, String id) {  
			this.threadsSignal = threadsSignal;
			this.index = index;
			this.dataS = dataS;
			this.id = id;
		}
		
		@Override  
		public void run() {
			try{
				for (int i = 0; i < 1000; i++){
					JSONObject newData;
					if (id.equals("loc")){
						newData = StaticDataService.getLocationDataJSON();
					}
					else if (id.equals("cat")){
						newData = StaticDataService.getCatDataJSON();
					}
					else{
						new RuntimeException().printStackTrace();
						throw new RuntimeException("[ERROR]unknown thread id");
					}
					boolean found = false;
					for (int j = 0; j < dataS.size() && !found; j++){
						//System.out.println(dataS.get(j).toString());
						if (newData.toString().length() == dataS.get(j).toString().length()){
							found = true;
						}
					}
					if (!found){
						//System.out.println(newData.toString());
						new RuntimeException().printStackTrace();
						throw new RuntimeException("[ERROR]Data not equal, thread index: " + this.index);
					}
				}
			} finally{
				threadsSignal.countDown();
			}
		}  
	}
	
	public class TestSetThread extends Thread {  
		private CountDownLatch threadsSignal;
		private int index;
		private ArrayList<JSONObject> dataS;
		private String id;
		
		public TestSetThread(CountDownLatch threadsSignal, int index, ArrayList<JSONObject> dataS, String id) {  
			this.threadsSignal = threadsSignal;
			this.index = index;
			this.dataS = dataS;
			this.id = id;
		}
		
		@Override  
		public void run() {
			
			try{
				for (int i = 0; i < 6000; i++){
					if (id.equals("loc")){
						Random random = new Random();
						int index = (random.nextInt(100)+1) % dataS.size();
						//System.out.println("Setting cache");
						StaticDataRamCache.setLocationData(dataS.get(index));
						
						if (random.nextInt(100) % 17 == 0){
							//System.out.println("Clearing cache");
							StaticDataRamCache.clear();
						}
					}
					else if (id.equals("cat")){
						Random random = new Random();
						int index = (random.nextInt(100)+1) % dataS.size();
						//System.out.println("Setting cache");
						StaticDataRamCache.setCatData(dataS.get(index));
						
						if (random.nextInt(100) % 17 == 0){
							//System.out.println("Clearing cache");
							StaticDataRamCache.clear();
						}
					}
					else{
						new RuntimeException().printStackTrace();
						throw new RuntimeException("[ERROR]unknown thread id, thread index: " + this.index);
					}
				}
			} finally{
				threadsSignal.countDown();
			}
		}  
	} 
	

	@Test
	public void test() throws InterruptedException {
		String cat_source1 = "{\"语文\":{\"高中\":\"\",\"初中\":\"\"},\"小语种\":{\"日语\":\"\",\"法语\":\"\",\"西班牙语\":\"\",\"韩语\":\"\",\"阿拉伯语\":\"\",\"Simon语\":\"\"},\"英语\":{\"四级\":\"\",\"SAT\":\"\",\"GRE\":\"\",\"托福\":\"\",\"六级\":\"\",\"雅思\":\"\"},\"考研\":{\"考研\":\"\"},\"数学\":{\"高中\":\"\",\"初中\":\"\"},\"会计\":{\"会计证\":\"\",\"职称证\":\"\",\"职业资格证\":\"\"}}";
		String cat_source2 = "{\"语文\":{\"高中\":\"\",\"初中\":\"\"},\"考研\":{\"考研\":\"\"},\"数学\":{\"高中\":\"\",\"初中\":\"\"},\"会计\":{\"会计证\":\"\",\"职称证\":\"\",\"职业资格证\":\"\"}}";
		String cat_source3 = "{\"语文\":{\"高中\":\"\"},\"小语种\":{\"日语\":\"\",\"Simon语\":\"\"},\"英语\":{\"四级\":\"\",\"SAT\":\"\",\"GRE\":\"\",\"托福\":\"\",\"六级\":\"\",\"雅思\":\"\"},\"考研\":{\"考研\":\"\"},\"数学\":{\"高中\":\"\",\"初中\":\"\"},\"会计\":{\"职称证\":\"\",\"职业资格证\":\"\"}}";
		String cat_source4 = "{\"语文\":{\"高中\":\"\",\"初中\":\"\",\"法语\":\"\",\"西班牙语\":\"\"},\"小语种\":{\"日语\":\"\",\"法语\":\"\",\"西班牙语\":\"\",\"韩语\":\"\",\"阿拉伯语\":\"\",\"Simon语\":\"\"},\"英语\":{\"四级\":\"\",\"SAT\":\"\"},\"考研\":{\"考研\":\"\"},\"数学\":{\"高中\":\"\",\"初中\":\"\"}}";
		JSONObject cat_json1 = new JSONObject(cat_source1);
		JSONObject cat_json2 = new JSONObject(cat_source2);
		JSONObject cat_json3 = new JSONObject(cat_source3);
		JSONObject cat_json4 = new JSONObject(cat_source4);
		ArrayList<JSONObject> catDataS = new ArrayList<JSONObject>();
		catDataS.add(cat_json1);
		catDataS.add(cat_json2);
		catDataS.add(cat_json3);
		catDataS.add(cat_json4);
		
		String location_source1 = "{\"南京\":{\"江宁区\":\"\",\"秦淮区\":\"\",\"浦口区\":\"\",\"六合区\":\"\",\"建邺区\":\"\",\"栖霞区\":\"\",\"高淳区\":\"\",\"溧水区\":\"\",\"玄武区\":\"\",\"雨花区\":\"\",\"鼓楼区\":\"\"},\"苏州\":{\"马修区\":\"\"}}";
		String location_source2 = "{\"南京\":{\"江宁区\":\"\",\"秦淮区\":\"\",\"浦口区\":\"\",\"高淳区\":\"\",\"溧水区\":\"\",\"玄武区\":\"\",\"雨花区\":\"\",\"鼓楼区\":\"\"},\"苏州\":{\"马修区\":\"\",\"拉拉区\":\"\"}}";
		String location_source3 = "{\"南京\":{\"江宁区\":\"\",\"秦淮区\":\"\",\"浦口区\":\"\",\"六合区\":\"\",\"建邺区\":\"\",\"栖霞区\":\"\",\"高淳区\":\"\",\"溧水区\":\"\",\"玄武区\":\"\",\"雨花区\":\"\",\"鼓楼区\":\"\"}}";
		String location_source4 = "{\"苏州\":{\"马修区\":\"\"}}";
		JSONObject location_json1 = new JSONObject(location_source1);
		JSONObject location_json2 = new JSONObject(location_source2);
		JSONObject location_json3 = new JSONObject(location_source3);
		JSONObject location_json4 = new JSONObject(location_source4);
		ArrayList<JSONObject> locationDataS = new ArrayList<JSONObject>();
		locationDataS.add(location_json1);
		locationDataS.add(location_json2);
		locationDataS.add(location_json3);
		locationDataS.add(location_json4);
		
		SystemDataInit.init();
		int readThreadNum = 1000;
		int setThreadNum = 100;
		CountDownLatch readThreadSignal = new CountDownLatch(readThreadNum);
		CountDownLatch setThreadSignal = new CountDownLatch(setThreadNum);
		
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < readThreadNum/2; i++){
			Thread testRun = new TestReadThread(readThreadSignal, i, catDataS, "cat");
			threads.add(testRun);
		}
		for (int i = 0; i < setThreadNum; i++){
			Thread testRun = new TestSetThread(setThreadSignal, i, catDataS, "cat");
			threads.add(testRun);
		}
		for (int i = readThreadNum/2; i < readThreadNum; i++){
			Thread testRun = new TestReadThread(readThreadSignal, i, catDataS, "cat");
			threads.add(testRun);
		}
		
		System.out.println("start time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		for (Thread testRun : threads){
			testRun.start();
		}
		readThreadSignal.await();
		setThreadSignal.await();
		System.out.println("middle time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		
		threads.clear();
		readThreadSignal = new CountDownLatch(readThreadNum);
		setThreadSignal = new CountDownLatch(setThreadNum);
		for (int i = 0; i < readThreadNum/2; i++){
			Thread testRun = new TestReadThread(readThreadSignal, i, locationDataS, "loc");
			threads.add(testRun);
		}
		for (int i = 0; i < setThreadNum; i++){
			Thread testRun = new TestSetThread(setThreadSignal, i, locationDataS, "loc");
			threads.add(testRun);
		}
		for (int i = readThreadNum/2; i < readThreadNum; i++){
			Thread testRun = new TestReadThread(readThreadSignal, i, locationDataS, "loc");
			threads.add(testRun);
		}
		
		for (Thread testRun : threads){
			testRun.start();
		}
		readThreadSignal.await();
		setThreadSignal.await();
		System.out.println("finish time: " + DateUtility.castToReadableString(DateUtility.getCurTimeInstance()));
		
	}

}
