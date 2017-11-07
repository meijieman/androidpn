package org.androidpn.server.dao.hibernate;

import org.androidpn.server.dao.PushDetailDao;
import org.androidpn.server.model.PushDetail;
import org.androidpn.server.util.CommonUtil;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

public class PushDetailDaoHibernate extends HibernateDaoSupport implements PushDetailDao {

    @Override
    public PushDetail savePushDetail(PushDetail pd) {
        getHibernateTemplate().saveOrUpdate(pd);
        getHibernateTemplate().flush();
        return pd;
    }

    @Override
    public List<PushDetail> getPushDetails() {
        return getHibernateTemplate().find("from PushDetail pd order by pd.createdDate desc");
    }

    @Override
    public PushDetail getPushDetail(String username, String UUID) {
        List<PushDetail> list = getHibernateTemplate().find("from PushDetail pd where username=? and uuid=?", new String[]{username, UUID});
        if (CommonUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }
}
