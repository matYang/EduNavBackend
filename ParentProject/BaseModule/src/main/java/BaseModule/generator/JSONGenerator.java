package BaseModule.generator;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import BaseModule.exception.validation.ValidationException;
import BaseModule.interfaces.PseudoModel;
import BaseModule.model.*;
import BaseModule.model.representation.*;

public class JSONGenerator {
	
	public static JSONObject toJSON(final PseudoModel obj) throws ValidationException{
		if (obj == null){
			return new JSONObject();
		}
		else if (obj instanceof User){
			return ((User)obj).toJSON();
		}
		else if (obj instanceof Partner){
			return ((Partner)obj).toJSON();
		}
		else if (obj instanceof Course){
			return ((Course)obj).toJSON();
		}
		else if (obj instanceof Booking){
			return ((Booking)obj).toJSON();
		}
		else if (obj instanceof AdminAccount){
			return ((AdminAccount)obj).toJSON();
		}
		else if (obj instanceof Credit){
			return ((Credit)obj).toJSON();
		}
		else if (obj instanceof Coupon){
			return ((Coupon)obj).toJSON();
		}
		else if (obj instanceof Transaction){
			return ((Transaction)obj).toJSON();
		}
		else if (obj instanceof CourseSearchRepresentation){
			return ((CourseSearchRepresentation)obj).toJSON();
		}
		else if (obj instanceof UserSearchRepresentation){
			return ((UserSearchRepresentation)obj).toJSON();
		}
		else if (obj instanceof PartnerSearchRepresentation){
			return ((PartnerSearchRepresentation)obj).toJSON();
		}
		else if (obj instanceof BookingSearchRepresentation){
			return ((BookingSearchRepresentation)obj).toJSON();
		}
		else if (obj instanceof AdminSearchRepresentation){
			return ((AdminSearchRepresentation)obj).toJSON();
		}
		else if (obj instanceof CouponSearchRepresentation){
			return ((CouponSearchRepresentation)obj).toJSON();
		}
		else if (obj instanceof CreditSearchRepresentation){
			return ((CreditSearchRepresentation)obj).toJSON();
		}
		else if (obj instanceof TransactionSearchRepresentation){
			return ((TransactionSearchRepresentation)obj).toJSON();
		}
		else{
			return new JSONObject();
		}
	}
	
	public static JSONArray toJSON(final ArrayList<? extends PseudoModel> objs) throws ValidationException{
		ArrayList<JSONObject> temps = new ArrayList<JSONObject>();
		if (objs == null){
			return new JSONArray();
		}
		for (int i = 0; i < objs.size(); i++){
			if (objs.get(i) != null){
				JSONObject jsonResult = toJSON(objs.get(i));
				temps.add(jsonResult);

			}
		}
		return new JSONArray(temps);
	}
	

}
