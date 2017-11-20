package com.roycom;

import com.roycom.linux.storage.Phy;

public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String json = Phy.getAllPhysAttrJson();
		System.out.println(json);
	}

}
