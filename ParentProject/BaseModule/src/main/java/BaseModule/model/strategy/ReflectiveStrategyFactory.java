package BaseModule.model.strategy;

import java.util.Map;

import org.json.JSONObject;

import BaseModule.configurations.EnumConfig.ReflectiveOp;
import BaseModule.interfaces.PseudoModel;
import BaseModule.interfaces.PseudoReflectiveStrategy;
import BaseModule.model.strategy.reflectiveStrategy.InitializationStrategy;
import BaseModule.model.strategy.reflectiveStrategy.StoreJSONStrategy;
import BaseModule.model.strategy.reflectiveStrategy.StoreKvpsStrategy;
import BaseModule.model.strategy.reflectiveStrategy.ToJSONStrategy;

public class ReflectiveStrategyFactory {
	
	private final ReflectiveOp op;
	private final PseudoModel model;
	private final JSONObject json;
	private final Map<String, String> kvps;
	
	public ReflectiveStrategyFactory(final ReflectiveOp op, final PseudoModel model, final JSONObject json, final Map<String, String> kvps){
		this.op = op;
		this.model = model;
		this.json = json;
		this.kvps = kvps;
	}
	
	public PseudoReflectiveStrategy getStrategy(){
		switch (op){
			case TOJSON:
				return new ToJSONStrategy(this.model, this.json);
				
			case STOREJSON:
				return new StoreJSONStrategy(this.model, this.json);
				
			case STOREKVPS:
				return new StoreKvpsStrategy(this.model, this.kvps);
			
			case INITIALIZE:
				return new InitializationStrategy(this.model);
				
			default:
				throw new RuntimeException("[ERR] ReflectiveStrategyFactory missing type: " + op);
		}
	}

}
