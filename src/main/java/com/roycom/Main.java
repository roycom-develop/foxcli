package com.roycom;

import java.util.ArrayList;

import com.roycom.linux.Common;
import com.roycom.linux.storage.Disk;
import com.roycom.linux.storage.instrument.ChipModel;

public class Main {

	public static void main(String[] args) {
		String json = "";
		ArrayList<String> tools = new ArrayList<String>();
		tools.add("smartctl");
		tools.add("lsscsi");
		try{
			boolean env = Common.checkEnvironmentTools(tools);
			if(!env){
				return;
			}
			Disk disk = new Disk(ChipModel.SAS3008, "sdg");	
			disk.fillAttr();
			json = disk.smartToJson();
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			System.out.println(json);
		}
		
	}

}
