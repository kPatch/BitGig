/*
 * Copyright (C) 2012 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bitgig.bitgig.common.executor;

import com.bitgig.bitgig.common.PlatformSupportManager;

public final class AsyncTaskExecManager extends PlatformSupportManager<AsyncTaskExecInterface> {

  public AsyncTaskExecManager() {
    super(AsyncTaskExecInterface.class, new DefaultAsyncTaskExecInterface());
    addImplementationClass(11, "com.coinbase.zxing.client.android.common.executor.HoneycombAsyncTaskExecInterface");
  }

}
