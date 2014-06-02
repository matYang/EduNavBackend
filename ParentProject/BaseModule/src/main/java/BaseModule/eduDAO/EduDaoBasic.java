package BaseModule.eduDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactory;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;
import net.spy.memcached.DefaultConnectionFactory;
import net.spy.memcached.DefaultHashAlgorithm;
import net.spy.memcached.KetamaNodeLocator;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.NodeLocator;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;
import net.spy.memcached.internal.OperationFuture;

import BaseModule.common.DebugLog;
import BaseModule.configurations.ServerConfig;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class EduDaoBasic {

	private static JedisPool jedisPool; 
	private static HikariDataSource ds;
	private static MemcachedClient memcached;
	
	static {
		JedisPoolConfig jedisConfig = new JedisPoolConfig();
		jedisConfig.setTestOnBorrow(false);
		jedisConfig.setMinIdle(5);
		jedisConfig.setMaxWait(4000l);
		jedisPool = new JedisPool(jedisConfig, ServerConfig.configurationMap.get("redisUri"), 6379);
		
		
		HikariConfig sqlConfig = new HikariConfig();
		sqlConfig.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
		sqlConfig.addDataSourceProperty("url", "jdbc:mysql://" + ServerConfig.configurationMap.get("jdbcUri"));
		sqlConfig.addDataSourceProperty("user", ServerConfig.configurationMap.get("sqlUser"));
		sqlConfig.addDataSourceProperty("password", ServerConfig.configurationMap.get("sqlPass"));
		sqlConfig.setPoolName("SQLPool");
		sqlConfig.setMaxLifetime(1800000l);
		sqlConfig.setAutoCommit(true);
		sqlConfig.setMaximumPoolSize(Integer.parseInt(ServerConfig.configurationMap.get("sqlMaxConnection")));
		sqlConfig.setConnectionTimeout(10000l);
		ds = new HikariDataSource(sqlConfig);
		
		
	   	try {
	   		if (!ServerConfig.configurationMap.get(ServerConfig.MAP_ENV_KEY).equals(ServerConfig.MAP_ENV_PROD)){
	   			DefaultConnectionFactory connectionFactory = new DefaultConnectionFactory(DefaultConnectionFactory.DEFAULT_OP_QUEUE_LEN, DefaultConnectionFactory.DEFAULT_READ_BUFFER_SIZE, DefaultHashAlgorithm.KETAMA_HASH);
	   			memcached = new MemcachedClient(connectionFactory, AddrUtil.getAddresses(ServerConfig.configurationMap.get("memcachedUri")));
	   		}
			else{
				AuthDescriptor ad = new AuthDescriptor(new String[]{"PLAIN"}, new PlainCallbackHandler(ServerConfig.configurationMap.get("memcachedUser"), ServerConfig.configurationMap.get("memcachedPass")));
				memcached = new MemcachedClient(new ConnectionFactoryBuilder().setProtocol(Protocol.BINARY).setOpTimeout(500).setAuthDescriptor(ad).build(), AddrUtil.getAddresses("ocs.aliyun.com:11211"));
			}
	    } catch (IOException e) {
	    	e.printStackTrace();
	        throw new RuntimeException("Memcache connection failed, please try again later");
	    }
		
	}	

    
    public static Jedis getJedis() {
        return jedisPool.getResource();
    }
    
    public static void returnJedis(Jedis jedis){
    	jedisPool.returnResource(jedis);
    }
    
    public static boolean shouldConnectionClose(Connection...connections){
    	return connections==null || connections.length==0;
    }
    public static Connection getSQLConnection(){
    	Connection connection;
    	try {
			connection = ds.getConnection();
		} catch (SQLException e) {
			DebugLog.d(e);
			throw new RuntimeException(e.getMessage(), e); 
		} 
		return connection;

    }
    
    public static Connection getConnection(Connection...connections){
    	if(connections.length==0){
    		return getSQLConnection();
    	}else if(connections.length==1&& connections[0] instanceof java.sql.Connection){
    		return connections[0];
    	}else return null;
    }
    
    public static void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs,boolean closeconn){
    	try{
			if (stmt != null)  stmt.close();  
			if (conn != null &&closeconn)  conn.close(); 
			if (rs != null) rs.close();
		} catch (SQLException e){
			DebugLog.d("Exception when closing stmt, rs and conn");
			DebugLog.d(e);
		}
    }
    
    public static void closeResources(Connection conn, Statement stmt, ResultSet rs,boolean closeconn){
    	try{
			if (stmt != null)  stmt.close();  
			if (conn != null && closeconn)  conn.close(); 
			if (rs != null) rs.close();
		} catch (SQLException e){
			DebugLog.d("Exception when closing stmt, rs and conn");
			DebugLog.d(e);
		}
    }
    
    public static Object getCache(String key){
    	return memcached.get(key);
    }
    
    public static OperationFuture<Boolean> setCache(String key, int exp, Object obj){
    	return memcached.set(key, 3600, obj);
    }

    
    public static OperationFuture<Boolean> deleteCache(String key){
    	return memcached.delete(key);
    }
    

    public static void clearAllDatabase(){
    	Jedis jedis = getJedis();
    	
        Statement stmt = null;
		Connection conn = null;
        String query0 = "SET FOREIGN_KEY_CHECKS=0 ";       
        String query1 = "TRUNCATE TABLE AdminAccountDao ";
        String query2 = "TRUNCATE TABLE UserDao ";
        String query3 = "TRUNCATE TABLE PartnerDao ";
        String query4 = "TRUNCATE TABLE CourseDao";
        String query5 = "TRUNCATE TABLE BookingDao";  
        String query6 = "TRUNCATE TABLE TransactionDao";
        String query7 = "TRUNCATE TABLE CouponDao";
        String query8 = "TRUNCATE TABLE CreditDao";
        String query9 = "SET FOREIGN_KEY_CHECKS=1;";
        try{
        	conn = getSQLConnection();
        	stmt = conn.createStatement();
        			
        	stmt.addBatch(query0);
        	stmt.addBatch(query1);
        	stmt.addBatch(query2);
        	stmt.addBatch(query3);
        	stmt.addBatch(query4);
        	stmt.addBatch(query5);
        	stmt.addBatch(query6);
        	stmt.addBatch(query7);  
        	stmt.addBatch(query8);
        	stmt.addBatch(query9);
        	stmt.executeBatch();
        	
        	memcached.flush();
            jedis.flushAll();
        } catch(SQLException e) {
        	DebugLog.d(e);
        } finally {
        	returnJedis(jedis);
			try{
				if (stmt != null)  stmt.close();  
	            if (conn != null)  conn.close(); 
			} catch (SQLException e){
				DebugLog.d("Exception when closing stmt, rs and conn");
				DebugLog.d(e);
			}
        }	

    }
}
