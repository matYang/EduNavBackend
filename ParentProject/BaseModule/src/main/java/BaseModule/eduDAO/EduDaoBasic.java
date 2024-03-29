package BaseModule.eduDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;
import net.spy.memcached.DefaultConnectionFactory;
import net.spy.memcached.DefaultHashAlgorithm;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;
import net.spy.memcached.internal.OperationFuture;
import BaseModule.cache.CourseRamCache;
import BaseModule.common.DebugLog;
import BaseModule.configurations.ParamConfig;
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
		jedisPool = new JedisPool(jedisConfig, ServerConfig.configurationMap.get("redisUri"), Integer.parseInt(ServerConfig.configurationMap.get("redisPort")));
		
		
		HikariConfig sqlConfig = new HikariConfig();
		sqlConfig.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
		sqlConfig.addDataSourceProperty("url", "jdbc:mysql://" + ServerConfig.configurationMap.get("jdbcUri"));
		sqlConfig.addDataSourceProperty("user", ServerConfig.configurationMap.get("sqlUser"));
		sqlConfig.addDataSourceProperty("password", ServerConfig.configurationMap.get("sqlPass"));
		sqlConfig.setPoolName("SQLPool");
		sqlConfig.setMaxLifetime(1800000l);
		sqlConfig.setAutoCommit(true);
		sqlConfig.setMaximumPoolSize(Integer.parseInt(ServerConfig.configurationMap.get("sqlMaxConnection")));
		sqlConfig.setConnectionTimeout(100000l);
		sqlConfig.addDataSourceProperty("useUnicode", "true");
		sqlConfig.addDataSourceProperty("characterEncoding", "utf8");
		ds = new HikariDataSource(sqlConfig);
		
		System.out.println("EduDaoBasic acknowledged config");
		
	   	try {
	   		if (ServerConfig.configurationMap.get(ParamConfig.MAP_ENV_KEY).equals(ParamConfig.MAP_ENV_LOCAL)){
	   			DefaultConnectionFactory connectionFactory = new DefaultConnectionFactory(DefaultConnectionFactory.DEFAULT_OP_QUEUE_LEN, DefaultConnectionFactory.DEFAULT_READ_BUFFER_SIZE, DefaultHashAlgorithm.KETAMA_HASH);
	   			memcached = new MemcachedClient(connectionFactory, AddrUtil.getAddresses(ServerConfig.configurationMap.get("memcachedUri")));
	   		}
			else{
				AuthDescriptor ad = new AuthDescriptor(new String[]{"PLAIN"}, new PlainCallbackHandler(ServerConfig.configurationMap.get("memcachedUser"), ServerConfig.configurationMap.get("memcachedPass")));
				memcached = new MemcachedClient(new ConnectionFactoryBuilder().setProtocol(Protocol.BINARY).setOpTimeout(500).setAuthDescriptor(ad).build(), AddrUtil.getAddresses(ServerConfig.configurationMap.get("memcachedUri")));
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
    
    private static Connection getSQLConnection(){
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
    	if(connections==null||connections.length==0){
    		return getSQLConnection();
    	}else return connections[0];
    }
    
    public static void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs,boolean closeconn){
    	try{
			if (stmt != null)  stmt.close();  
		} catch (SQLException e){
			DebugLog.d("Exception when closing stmt, rs and conn");
			DebugLog.d(e);
		}
    	try{
    		if (conn != null &&closeconn){
    			conn.close();
    		}
    	} catch (SQLException e){
			DebugLog.d("Exception when closing stmt, rs and conn");
			DebugLog.d(e);
		}
    	try{
    		if (rs != null) rs.close();
    	} catch (SQLException e){
			DebugLog.d("Exception when closing stmt, rs and conn");
			DebugLog.d(e);
		}
    }
    
    public static boolean handleCommitFinally(Connection conn, boolean ok, boolean shouldCloseConnection){
    	try{
			if (conn != null && !conn.getAutoCommit()){				
				if (!ok){
					conn.rollback();
					conn.setAutoCommit(true);
				}
				else if(ok){
					conn.commit();
					return true;
				}				
			}
			return false;
		} catch (SQLException e){
			DebugLog.d(e);
			return false;
		} finally{
			EduDaoBasic.closeResources(conn, null, null, shouldCloseConnection);
		}
    }
    
    public static Object getCache(String key){
    	try{
    		return memcached.get(key);
    	} catch (Exception e){
    		DebugLog.d("[ERROR] memcached getCache failed");
    		return null;
    	}

    }
    
    public static Map<String, Object> getBulkCache(Collection<String> keys){
    	try{
    		return memcached.getBulk(keys);
    	} catch (Exception e){
    		DebugLog.d("[ERROR] memcached getBulkCache failed");
    		return new HashMap<String, Object>();
    	}
    }
    
    public static OperationFuture<Boolean> setCache(String key, int exp, Object obj){
    	return memcached.set(key, exp, obj);
    }

    public static OperationFuture<Boolean> addCache(String key, int exp, Object obj){
    	return memcached.add(key, exp, obj);
    }
    
    public static OperationFuture<Boolean> deleteCache(String key){
    	return memcached.delete(key);
    }
    

    public static void clearAllDatabase(){
    	CourseRamCache.clear();
    	Jedis jedis = getJedis();
    	
        Statement stmt = null;
		Connection conn = null;
        String query0 = "SET FOREIGN_KEY_CHECKS=0 ";       
        String query1 = "TRUNCATE TABLE AdminAccount ";
        String query2 = "TRUNCATE TABLE User ";
        String query3 = "TRUNCATE TABLE Partner ";
        String query4 = "TRUNCATE TABLE Course";
        String query5 = "TRUNCATE TABLE Booking";  
        String query6 = "TRUNCATE TABLE Transaction";
        String query7 = "TRUNCATE TABLE Coupon";
        String query8 = "TRUNCATE TABLE Credit";
        String query9 = "TRUNCATE TABLE Teacher";
        String query10 = "TRUNCATE TABLE ClassPhoto";
        String query11 = "SET FOREIGN_KEY_CHECKS=1;";
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
        	stmt.addBatch(query10);
        	stmt.addBatch(query11);
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
