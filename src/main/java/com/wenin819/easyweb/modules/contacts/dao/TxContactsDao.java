/**
* There are <a href="https://github.com/wenin819/easyweb">EasyWeb</a> code generation
*/
package com.wenin819.easyweb.modules.contacts.dao;

import com.wenin819.easyweb.core.persistence.mybatis.MybatisBaseDao;
import com.wenin819.easyweb.modules.contacts.model.TxContacts;

import org.springframework.stereotype.Repository;

/**
 * @author wenin819@gmail.com
 */
@Repository
public interface TxContactsDao extends MybatisBaseDao<TxContacts> {

}
