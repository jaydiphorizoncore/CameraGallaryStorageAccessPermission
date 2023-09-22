package com.example.cameragallarystorageaccesspermission.util

import java.io.IOException

class ApiException(message: String) : IOException(message)
class UnAuthorisedException(message: String) : IOException(message)
class AppVersionException(message: String) : IOException(message)