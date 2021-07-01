package com.phone91.sdk.utils


object Constants {

    val PREF_NAME = "perf_phone91_sdk"
    val UPDATE_ID :Long= 123456789
    val DEFAULT_CATEGORY_ID :Long= 3120
    val DEFAULT_CATEGORY_NAME :String="Uncategorized"
    val SERVER_URL :String=""
    var STRIPE_TEST_KEY :String="pk_test_cDEANZnpxGVqgFmo4gwqQ6QC"
//    var STRIPE_TEST_KEY :String="pk_test_9G6CAj0lb6pGqIO3gxJmOyBl00McT3bBNw"
    var STRIPE_LIVE_KEY :String="pk_test_voUSp3UyZB352AaFUb7ZGJ5Z00tfVHDZoK"
    var PLAID_PUBLIC_KEY :String="d705987df9e953da9d39b9f35833d3"




    interface KEY {
        companion object {
            val DEAL = "key_deal"
            val FLAG = "flag"
        }
    }

    interface CALENDAR {
        companion object {
            val CALENDAR_ID = 1
            val ADD_EVENT = 2
            val UPDATE_EVENT = 3
            val DELETE_EVENT = 4
        }
    }

    interface API {
        interface KEY {
            companion object {
                val STATUS = "status"
                val ERROR = "error"
                val CODE = "code"
                val MESSAGE = "msg"
                val APPLICATION_ID = "application_id"
                val DATA = "data"
                val UNIVERSITIES = "universities"
                val COURSES = "courses"
                val COUNTRIES = "countries"
                val DOCUMENTS = "documents"
            }
        }

        interface STATUS {
            companion object {
                val ERROR = "error"
                val OK = "OK"
                val ZERO = "0"
                val ONE = "1"
            }
        }
    }
}