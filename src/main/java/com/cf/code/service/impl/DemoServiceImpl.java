/**
 * 
 */
package com.cf.code.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.cf.code.core.exception.BusinessException;
import com.cf.code.dao.DemoDao;
import com.cf.code.entity.Demo;
import com.cf.code.entity.enums.DemoType;
import com.cf.code.service.DemoService;

/**
 *
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 *
 */
@Service("demoService")
public class DemoServiceImpl implements DemoService {
	
	private static Logger log = LogManager.getLogger(DemoServiceImpl.class);
 
	@Resource(name = "demoDao")
	DemoDao demoDao;
	
	@Resource(name = "demoDaoRead")
	DemoDao demoDaoRead;
	
	@Override
	public Integer insert(String name) {
		Demo demo = new Demo();
		demo.setName(name);
		if(this.demoDao.insert(demo)){
			return demo.getId();
		}
		return 0;
	}

	@Override
	public boolean delete(Integer id) {
		return this.demoDao.delete(id);
	}

	@Override
	public void update(Integer id, String name) throws BusinessException {
		boolean b = this.demoDao.update(id, name,null);
		if(!b){
			throw new BusinessException("id不存在，更新失败");
		}
	}

	@Override
	public Demo find(Integer id) {
		log.info("DemoId["+id+"]-"+this);
		return this.demoDaoRead.find(id);
	}

	@Override
	public List<Demo> query(Date time) {
		return this.demoDaoRead.query(time,null);
	}

	private void txUpdate(){
		Demo demo = new Demo();
		demo.setName("事务操作插入2");
		this.demoDao.insert(demo);
		if(true){
			throw new RuntimeException("事务跑出运行期异常");
		}
		Demo demo1 = new Demo();
		demo1.setName("事务操作插入3");
		this.demoDao.insert(demo1);
	}

	@Override
	public void tpTxUpdate(DemoType dt) throws BusinessException {
		log.info("d");
		boolean b = this.demoDao.update(9,"",dt);
		log.info("dd");
		if(!b){
			throw new BusinessException("name已更改");
		}
		Demo demo1 = new Demo();
		demo1.setName("事务操作插入3");
		this.demoDao.insert(demo1);
	}

	@Override
	public void it4Async() {
		System.out.println("------it4Async---------start");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("------it4Async---------end");
	}
	
}
