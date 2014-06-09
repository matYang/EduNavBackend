package BaseModule.concurrentTest;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.json.JSONArray;
import org.junit.Test;

import BaseModule.cache.StaticDataRamCache;
import BaseModule.common.DateUtility;
import BaseModule.staticDataService.StaticDataService;

public class ConcurrentStaticDataCacheTest {
	
	public class TestReadThread extends Thread {  
		private CountDownLatch threadsSignal;
		private int index;
		private ArrayList<JSONArray> dataS;
		private String id;
		
		public TestReadThread(CountDownLatch threadsSignal, int index, ArrayList<JSONArray> dataS, String id) {  
			this.threadsSignal = threadsSignal;
			this.index = index;
			this.dataS = dataS;
			this.id = id;
		}
		
		@Override  
		public void run() {
			try{
				for (int i = 0; i < 1000; i++){
					JSONArray newData;
					if (id.equals("loc")){
						newData = StaticDataService.getLocationDataJSON();
					}
					else if (id.equals("cat")){
						newData = StaticDataService.getCatDataJSON();
					}
					else{
						throw new RuntimeException("[ERROR]unknown thread id");
					}
					boolean found = false;
					for (int j = 0; j < dataS.size() && !found; j++){
						if (newData.toString().equals(dataS.get(j).toString())){
							found = true;
						}
					}
					if (!found){
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
		private ArrayList<JSONArray> dataS;
		private String id;
		
		public TestSetThread(CountDownLatch threadsSignal, int index, ArrayList<JSONArray> dataS, String id) {  
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
						System.out.println("Setting cache");
						StaticDataRamCache.setLocationData(dataS.get(index));
						
						if (random.nextInt(100) % 17 == 0){
							System.out.println("Clearing cache");
							StaticDataRamCache.clear();
						}
					}
					else if (id.equals("cat")){
						Random random = new Random();
						int index = (random.nextInt(100)+1) % dataS.size();
						System.out.println("Setting cache");
						StaticDataRamCache.setCatData(dataS.get(index));
						
						if (random.nextInt(100) % 17 == 0){
							System.out.println("Clearing cache");
							StaticDataRamCache.clear();
						}
					}
					else{
						throw new RuntimeException("[ERROR]unknown thread id");
					}
				}
			} finally{
				threadsSignal.countDown();
			}
		}  
	} 
	

	@Test
	public void test() throws InterruptedException {
		String cat_source1 = "[{\"数学\":[\"初中\",\"高中\"]},{\"语文\":[\"初中\",\"高中\"]},{\"英语\":[\"四级\",\"六级\",\"托福\",\"雅思\",\"SAT\",\"GRE\"]},{\"考研\":[\"考研\"]},{\"会计\":[\"会计证\",\"职称证\",\"职业资格证\"]},{\"小语种\":[\"日语\",\"韩语\",\"法语\",\"西班牙语\",\"阿拉伯语\",\"Simon语\"]}]";
		String cat_source2 = "[{\"数学\":[\"初中\",\"高中\"]},{\"语文\":[\"初中\",\"高中\"]},{\"英语\":[\"四级\",\"六级\",\"托福\",\"雅思\",\"SAT\",\"GRE\"]},{\"考研\":[\"考研\"]},{\"会计\":[\"会计证\",\"职称证\",\"职业资格证\"]}]";
		String cat_source3 = "[{\"英语\":[\"四级\",\"六级\",\"托福\",\"雅思\",\"SAT\",\"GRE\"]},{\"考研\":[\"考研\"]},{\"会计\":[\"会计证\",\"职称证\",\"职业资格证\"]},{\"小语种\":[\"日语\",\"韩语\",\"法语\",\"西班牙语\",\"阿拉伯语\",\"Simon语\"]}]";
		String cat_source4 = "[{\"会计\":[\"会计证\",\"职称证\",\"职业资格证\"]}]";
		JSONArray cat_json1 = new JSONArray(cat_source1);
		JSONArray cat_json2 = new JSONArray(cat_source2);
		JSONArray cat_json3 = new JSONArray(cat_source3);
		JSONArray cat_json4 = new JSONArray(cat_source4);
		ArrayList<JSONArray> catDataS = new ArrayList<JSONArray>();
		catDataS.add(cat_json1);
		catDataS.add(cat_json2);
		catDataS.add(cat_json3);
		catDataS.add(cat_json4);
		
		String location_source1 = "[{\"南京\":[\"玄武区\",\"秦淮区\",\"建邺区\",\"鼓楼区\",\"浦口区\",\"栖霞区\",\"雨花区\",\"江宁区\",\"六合区\",\"溧水区\",\"高淳区\"]},{\"苏州\":[\"马修区\"]}]";
		String location_source2 = "[{\"南京\":[\"玄武区\",\"秦淮区\",\"建邺区\",\"鼓楼区\",\"栖霞区\",\"雨花区\",\"江宁区\",\"溧水区\"]},{\"苏州\":[\"马修区\"]}]";
		String location_source3 = "[{\"南京\":[\"鼓楼区\",\"浦口区\",\"栖霞区\",\"雨花区\",\"江宁区\",\"六合区\",\"溧水区\",\"高淳区\"]},{\"苏州\":[\"啦啦区\"]}]";
		String location_source4 = "[{\"南京\":[\"玄武区\",\"秦淮区\",\"建邺区\",\"江宁区\",\"六合区\",\"溧水区\",\"高淳区\"]},{\"苏州\":[\"皮卡丘区\"]}]";
		JSONArray location_json1 = new JSONArray(location_source1);
		JSONArray location_json2 = new JSONArray(location_source2);
		JSONArray location_json3 = new JSONArray(location_source3);
		JSONArray location_json4 = new JSONArray(location_source4);
		ArrayList<JSONArray> locationDataS = new ArrayList<JSONArray>();
		locationDataS.add(location_json1);
		locationDataS.add(location_json2);
		locationDataS.add(location_json3);
		locationDataS.add(location_json4);
		
		
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
