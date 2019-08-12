package configuration

import java.util.ArrayList

class JasmineData {
    var failures: ArrayList<Failure> = ArrayList()
}

class Failure(val fileName: String, val line: Int, val col: Int, val message: String) {

}