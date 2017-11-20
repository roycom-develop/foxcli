package com.roycom.linux.storage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roycom.linux.Common;
import com.roycom.linux.storage.instrument.Dev;
import com.roycom.linux.storage.instrument.InputException;

/**
 * ���̽ӿ�����
 * @author Stevy
 *
 */
enum Interface {
	SATA, SAS, NVMe
}

public class Disk implements Dev {
	private String model;
	private String fw;
	private String sn;
	private Interface interfaces;
	private String devName;
	private ChipModel fromChip;
	private String smartStr;
	private Map<String, Map<String, String>> smart_map = null;
	private String smartAll;
	
	/**
	 * Disk Disk��Ĺ��캯��
	 * @param from_chip ��������Ϊdev_name�Ŀ���оƬ�ͺ�
	 * @param dev_name �������ƣ�����sda
	 */
	public Disk(ChipModel from_chip, String dev_name){
		model = fw = sn = "";
		smartStr = "smartctl -a";
		if(!dev_name.matches("sd[a-z]*")){
			devName = "";
			throw new InputException("Disk.this(from_chip, dev_name): dev_name is invalid!");
		}else{
			devName = dev_name;
		}
		setFromChip(from_chip);
	}
	
	/**
	 * fillAttr �����̵���������
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Override
	public void fillAttr() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		Pattern pattern = null;
		Matcher matcher = null;
		String smartCmd = String.format("%s /dev/%s",smartStr, devName);
		smartAll = Common.exeShell(smartCmd);
		sn = Common.searchRegexString(smartAll, ".*Serial.*", " ", 2).get(0);
		fw = Common.searchRegexString(smartAll, "(^Firmware Version.*)|(^Revision.*)", ":", 1).get(0);
		model = Common.searchRegexString(smartAll, "(^Product.*)|(.*Model.*)", ":", 1).get(0);
		boolean isSata = smartAll.matches("(\n|.)*SATA(.|\n)*");
		boolean isSas = smartAll.matches("(\n|.)*SAS(.|\n)*");
		boolean isNvme = smartAll.matches("(\n|.)*NVM(.|\n)*");
		if(isSata){
			interfaces = Interface.SATA;
			pattern = Pattern.compile(".*  0x.*-*", Pattern.MULTILINE);
			matcher = pattern.matcher(smartAll);
			while(matcher.find()){
				Map<String, String> m1 = new HashMap<String, String>();
				String line_smart = matcher.group().trim();
				String[] arr = line_smart.split(" *");
				m1.put("FLAG", arr[2]);
				m1.put("VALUE", arr[3]);
				m1.put("WORST", arr[4]);
				m1.put("THRESH", arr[5]);
				m1.put("TYPE", arr[6]);
				m1.put("UPDATED", arr[7]);
				m1.put("WHEN_FAILED", arr[8]);
				m1.put("RAW_VALUE", arr[9]);
				smart_map.put(arr[1], m1);
			}
		}
		if(isSas) {
			interfaces = Interface.SAS;
			pattern = Pattern.compile(".*(read:|write:|verify:).*", Pattern.MULTILINE);
			matcher = pattern.matcher(smartAll);
			while(matcher.find()){
				Map<String, String> m1 = new HashMap<String, String>();
				String line_smart = matcher.group().trim();
				String[] arr = line_smart.split(" *");
				m1.put("ECC_fast", arr[1]);
				m1.put("ECC_delayed", arr[2]);
				m1.put("rerw", arr[3]);
				m1.put("errors_corrected", arr[4]);
				m1.put("algorithm_invocations", arr[5]);
				m1.put("processed_10^9_bytes", arr[6]);
				m1.put("uncorrected_error", arr[7]);
				smart_map.put(arr[0], m1);
			}
		}
		if(isNvme)
			interfaces = Interface.NVMe;
	}
	
	/**
	 * smartToJson �������̵ĳ�Աsmart��Ϣ��ʽ����json��ʽ
	 * @return ����json��ʽ��smart��Ϣ�ַ���
	 * @throws JsonProcessingException
	 */
	public String smartToJson() throws JsonProcessingException{
		String sataSmartJsonStr = null;
		ObjectMapper mapper = new ObjectMapper();
		sataSmartJsonStr = mapper.writeValueAsString(smart_map);
		return sataSmartJsonStr;
	}
	
	/**
	 * smartToString ��ӡ�������smart��Ϣ
	 */
	public void smartToString(){
		System.out.print(smartAll);
	}
	
	/**
	 * getSmart_map ��ȡsmart_map��Ա������Map<String, Map<String, String>>����ֵ
	 * @return
	 */
	public Map<String, Map<String, String>> getSmart_map() {
		return smart_map;
	}
	
	/**
	 * getSmartAll ��ȡ����smart��Ϣ��String����ֵ
	 * @return
	 */
	public String getSmartAll() {
		return smartAll;
	}

	/**
	 * getSn ��ȡ����SN��
	 * @return
	 */
	public String getSn() {
		return sn;
	}
	
	/**
	 * ���ô���SN��
	 * @param sn
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}
	
	/**
	 * ��ȡ���̽ӿ�Э�����ƣ�����SAS��SATA��NVMe
	 * @return
	 */
	public Interface getInterfaces() {
		return interfaces;
	}
	
	/**
	 * ���ô��̽ӿ�Э�����ƣ�����SAS��SATA��NVMe
	 * @return
	 */
	public void setInterfaces(Interface interfaces) {
		this.interfaces = interfaces;
	}

	/**
	 * ��ȡ�����ͺ�ȫ��
	 * @return
	 */
	public String getModel() {
		return model;
	}

	/**
	 * ���ô����ͺ�ȫ��
	 * @param model
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * ��ȡ���̹̼��汾
	 * @return
	 */
	public String getFw() {
		return fw;
	}

	/**
	 * ���ô��̹̼��汾
	 * @return
	 */
	public void setFw(String fw) {
		this.fw = fw;
	}

	/**
	 * ��ȡ�����豸����
	 * @return
	 */
	public String getDevName() {
		return devName;
	}

	/**
	 * ��ȡ�������ӵĿ�����оƬ�ͺ�
	 * @return
	 */
	public ChipModel getFromChip() {
		return fromChip;
	}

	public void setFromChip(ChipModel fromChip) {
		this.fromChip = fromChip;
	}

	/**
	 * ��ȡsmartctl�������ַ���
	 * @return
	 */
	public String getSmartStr() {
		return smartStr;
	}
}