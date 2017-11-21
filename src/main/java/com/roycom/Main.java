package com.roycom;

import java.util.ArrayList;

import com.roycom.linux.Common;
import com.roycom.linux.storage.Disk;
import com.roycom.linux.storage.instrument.ChipModel;

public class Main {

	public void function1(){
		String json = "";
		ArrayList<String> tools = new ArrayList<String>();
		tools.add("smartctl");
		tools.add("lsscsi");
		tools.add("storcli");
		tools.add("sas3ircu");
		tools.add("sas2ircu");
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
	
	public static void main(String[] args) {
		String x = "e23 abc 2 45\nabc 0 12";
		ArrayList<String> a = Common.searchRegexString(x, ".*abc.*", " ", 1);
		System.out.println(a.toString());
	}

}
