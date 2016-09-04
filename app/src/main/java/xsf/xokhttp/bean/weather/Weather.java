package xsf.xokhttp.bean.weather;

import java.io.Serializable;
import java.util.List;

/**
 * 说明：天气信息model
 */

public class Weather implements Serializable{
	public int error;
	public String status;
	public String date;
	public List<Result> results;

	@Override
	public String toString() {
		return "Weather [date=" + date + ", error=" + error + ", status="
				+ status + ", results=" + results + "]";
	}





}
