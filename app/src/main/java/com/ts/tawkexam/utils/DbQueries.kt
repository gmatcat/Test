package com.ts.tawkexam.utils

const val FETCH_ALL_USERS = "SELECT * FROM user"
const val FETCH_USER_BY_NAME = "SELECT * FROM user WHERE name LIKE :name"
const val FETCH_USER_BY_ID = "SELECT * FROM user WHERE name LIKE :id"