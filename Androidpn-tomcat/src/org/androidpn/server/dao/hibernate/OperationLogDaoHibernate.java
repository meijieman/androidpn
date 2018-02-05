package org.androidpn.server.dao.hibernate;

import org.androidpn.server.dao.OperationLogDao;
import org.androidpn.server.model.OperationLog;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.Date;
import java.util.List;

public class OperationLogDaoHibernate extends HibernateDaoSupport implements OperationLogDao {

    @Override
    public void saveOperationLog(OperationLog operationLog) {
        getHibernateTemplate().saveOrUpdate(operationLog);
        getHibernateTemplate().flush();
    }

    @Override
    public OperationLog getOperationLogFromCreatedDate(Date createDate) {
        List<OperationLog> list = getHibernateTemplate().find("from OperationLog o where o.createdDate=?", createDate);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<OperationLog> getOperationLogs() {
        return getHibernateTemplate().find("from OperationLog");
    }
}
