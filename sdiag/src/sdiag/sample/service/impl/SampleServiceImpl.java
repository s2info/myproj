package sdiag.sample.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import sdiag.sample.dao.SampleDAO;
import sdiag.sample.dao.SampleOtherDAO;
import sdiag.sample.service.SampleService;

@Service("sampleService")
public class SampleServiceImpl implements SampleService {

	@Resource(name="sampleDAO")
	private SampleDAO sampleDAO;
	
	@Resource(name="sampleOtherDAO")
	private SampleOtherDAO sampleOtherDAO;
	
	public void test() throws Exception{
		int cnt = sampleDAO.test();
		System.out.println("cnt = " + cnt);
	}
}
