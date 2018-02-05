package org.androidpn.server.service;

import org.androidpn.server.model.OperationLog;

import java.util.Date;
import java.util.List;

public interface OperationLogService {

    void saveOperationLog(OperationLog operationLog);

    OperationLog getOperationLogFromCreatedDate(Date createDate);

    List<OperationLog> getOperationLogs();
}
