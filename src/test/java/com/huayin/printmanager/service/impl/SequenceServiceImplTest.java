package com.huayin.printmanager.service.impl;

import org.junit.Test;

import com.huayin.printmanager.BaseJunit4Test;

public class SequenceServiceImplTest extends BaseJunit4Test 
{
	@Test
	public void testGetSequenceString()
	{
		/*System.out.println(serviceFactory.getSequenceService().getSequence("test_seq"));
		DynamicQuery seqQuery=new DynamicQuery(Sequence.class);
		seqQuery.eq("name", "test_seq");
		Sequence seq=serviceFactory.getDaoFactory().getCommonDao().getByDynamicQuery(seqQuery,Sequence.class);
	if(seq==null)
		seq=new Sequence();
		seq.setName("test_seq2");
		serviceFactory.getPersistService().save(seq);
		
		System.out.println(serviceFactor.getSequenceService().getSequence("test_seq",2));
		System.out.println(serviceFactory.getSequenceService().getSequence("test_seq",2));
*/
		for(int i=0;i<10;i++)
		System.out.println(serviceFactory.getSequenceService().getNoCacheSequence("nocache_test"));
	}

}
