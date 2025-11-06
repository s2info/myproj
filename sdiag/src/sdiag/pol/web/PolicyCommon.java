package sdiag.pol.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import egovframework.rte.psl.dataaccess.util.EgovMap;

public class PolicyCommon {

	public static List<EgovMap> getListFromContentForString(List<EgovMap> contList, String columnName, String columnValue)
	{
		List<EgovMap> list = new ArrayList<EgovMap>();
		
		for(EgovMap cont : contList)
		{
			if(cont.get(columnName).equals(columnValue))
			{
				list.add(cont);
			}
		}
		
		return list;
		
	}
	
	public static List<EgovMap> SelectedHashMapFromEgovMap(String sKey, String sValue, List<EgovMap> eMap)
	{
		List<EgovMap> list = new ArrayList<EgovMap>();
		for(EgovMap rows : eMap)
		{
			if(rows.containsKey(sKey))
			{
				if(String.format("%s", rows.get(sKey)) == sValue)
				{
					EgovMap map = new EgovMap();
					for(java.util.Iterator<EgovMap> iterator = eMap.iterator(); iterator.hasNext();)
					{
						// String keyName = (String) iterator.next();
		                // String valueName = (String) map.get(keyName);
					//	map.put((String) iterator.next(), value);
					}
					//list.put(key, value)
				}
			}
		}
		
		return list;
	}
}
