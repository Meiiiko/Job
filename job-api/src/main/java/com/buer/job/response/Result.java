package com.buer.job.response;


import com.buer.job.exception.JobException;

public interface Result<T> {
  Result originOk();

  Result originOk(T body);

  Result serverException(JobException exception);
}
