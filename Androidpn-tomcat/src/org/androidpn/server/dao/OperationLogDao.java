package org.androidpn.server.dao;

import org.androidpn.server.model.OperationLog;

import java.util.Date;
import java.util.List;

public interface OperationLogDao {

    void saveOperationLog(OperationLog operationLog);

    OperationLog getOperationLogFromCreatedDate(Date createDate);

    List<OperationLog> getOperationLogs();
}
