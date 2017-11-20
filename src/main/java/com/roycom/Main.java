package com.roycom;

import com.roycom.linux.storage.Disk;
import com.roycom.linux.storage.instrument.ChipModel;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Controller con = new Controller(ChipModel.SAS3008);
		Disk disk = new Disk(ChipModel.SAS3008, "sdg");
		String json = null;
		try{
			disk.fillAttr();
			json = disk.smartToJson();
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			System.out.print(json);
		}
		
	}

}
