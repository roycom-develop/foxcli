package com.roycom.linux.storage;

import java.io.IOException;
import java.util.ArrayList;

import com.roycom.linux.Common;
import com.roycom.linux.storage.instrument.ChipModel;
import com.roycom.linux.storage.instrument.Dev;


public class Controller implements Dev {
	private ChipModel chipModel;
	private String vendor;
	private int speed;
	private String fw;
	private boolean hasBBU;
	private ArrayList<Disk> disks;
	private int cacheSize;
	private int index;
	
	public Controller(ChipModel chipModel, int index){
		setChipModel(chipModel);
		setIndex(index);
	}
	
	public static void scanControllers(){
		String sas3Str = null;
		String sas2Str = null;
		String raidStr = null;
		try {
			sas3Str = Common.exeShell("sas3ircu list");
			sas2Str = Common.exeShell("sas2ircu list");
			raidStr = Common.exeShell("storcli show");
			ArrayList<String> sas3ChipModeArr = Common.searchRegexString(sas3Str, ".*SAS[0-9]{4}.*", " ", 1);
			ArrayList<String> sas3ChipIndexArr = Common.searchRegexString(sas3Str, ".*SAS[0-9]{4}.*", " ", 0);
			ArrayList<Controller> conArr = new ArrayList<Controller>();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 填充所有成员变量值
	 */
	@Override
	public void fillAttr() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		fillChipAttr();
	}
	/**
	 * 填充SAS芯片相关成员变量值
	 */
	private void fillChipAttr(){
		switch(chipModel){
		case SAS3008:
			setVendor("lsi");
			setCacheSize(0);
			setSpeed(12);
			setHasBBU(false);
			break;
		case SAS2308:
		case SAS2008:
			setVendor("lsi");
			setCacheSize(0);
			setSpeed(6);
			setHasBBU(false);
			break;
		case LSI2108:
			setVendor("lsi");
			setCacheSize(512);
			setSpeed(6);
			break;
		case LSI3108:
			setVendor("lsi");
			setSpeed(12);
			break;
		default:
			break;
		}
	}
	
	public void scanDisk(){
		switch (chipModel) {
		case SAS3008:
			break;
		case SAS2308:
		case SAS2008:
			break;
		case LSI3108:
		case LSI2108:
			break;
		default:
			break;
		}
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public ChipModel getChipModel() {
		return chipModel;
	}

	public void setChipModel(ChipModel chipModel) {
		this.chipModel = chipModel;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public String getFw() {
		return fw;
	}

	public void setFw(String fw) {
		this.fw = fw;
	}

	public boolean isHasBBU() {
		return hasBBU;
	}

	public void setHasBBU(boolean hasBBU) {
		this.hasBBU = hasBBU;
	}

	public ArrayList<Disk> getDisks() {
		return disks;
	}

	public void setDisks(ArrayList<Disk> disks) {
		this.disks = disks;
	}

	public int getCacheSize() {
		return cacheSize;
	}

	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
