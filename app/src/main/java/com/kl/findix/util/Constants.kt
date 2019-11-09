package com.kl.findix.util

import android.os.Environment

internal const val REQUEST_CODE_SIGN_IN = 9001
internal const val REQUEST_CODE_PERMISSION = 9002
internal const val REQUEST_CODE_CHOOOSE_PROFILE_ICON = 9003

internal var ROOT_DIR = Environment.getExternalStorageDirectory().path
internal var PICTURES = "$ROOT_DIR /Picture"
internal var CAMERA = "$ROOT_DIR + /DCIM/CAMERA"
internal var FIREBASE_IMAGE_STORAGE = "photos/users/"