package org.androidpn.server.service.impl;

import org.androidpn.server.dao.OperationLogDao;
import org.androidpn.server.model.OperationLog;
import org.androidpn.server.service.OperationLogService;

import java.util.Date;
import java.util.List;

public class OperationLogServiceImpl implements OperationLogService {

    private OperationLogDao dao;

    public void setOperationLogDao(OperationLogDao dao) {
        this.dao = dao;
    }

    @Override
    public void saveOperationLog(OperationLog operationLog) {
        dao.saveOperationLog(operationLog);
    }

    @Override
    public OperationLog getOperationLogFromCreatedDate(Date createDate) {
        return dao.getOperationLogFromCreatedDate(createDate);
    }

    @Override
    public List<OperationLog> getOperationLogs() {
        return dao.getOperationLogs();
    }
}
