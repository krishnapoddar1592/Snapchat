package com.example.snapchat

import java.lang.Exception

class Main {
    fun sortByKey(hm: HashMap<Int, String>?): HashMap<Int, String>? {
        // Create a list from elements of HashMap
        val list: List<HashMap.Entry<Int, String>> = LinkedList<HashMap.Entry<Int, String>>(
            hm.entrySet()
        )

        // Sort the list using lambda expression
        Collections.sort(
            list
        ) { i1, i2 -> i1.getKey().compareTo(i2.getKey()) }

        // put data from sorted list to hashmap
        val temp: HashMap<Int, String> = LinkedHashMap()
        for (aa in list) {
            temp[aa.getKey()] = aa.getValue()
        }
        return temp
    }

    fun main(args: Array<String>) {
        val `in` = Scanner(System.`in`)
        val budget: Int = `in`.nextInt()
        val players: HashMap<Int, HashMap> = HashMap<Int, HashMap>()
        for (i in 0..4) {
            val num: Int = `in`.nextInt()
            val map = HashMap<Int, String>()
            `in`.nextLine()
            for (j in 0 until num) {
                val input: String = `in`.nextLine()
                var strNums: Array<String>
                strNums = input.split(" ").toTypedArray()
                map[Integer.valueOf(strNums[1])] = strNums[0]
            }
            players[i] = map
        }
        var sum = 0
        var max_sum = 0
        var out = ""
        var map0: HashMap<Int, String>? = players[0]
        var map1: HashMap<Int, String>? = players[1]
        var map2: HashMap<Int, String>? = players[2]
        var map3: HashMap<Int, String>? = players[3]
        var map4: HashMap<Int, String>? = players[4]
        map0 = sortByKey(map0)
        map1 = sortByKey(map1)
        map2 = sortByKey(map2)
        map3 = sortByKey(map3)
        map4 = sortByKey(map4)
        if (map0!!.size() === 1 && map1!!.size() === 1 && map2!!.size() === 1 && map3!!.size() === 1 && map4!!.size() === 1) {
            out = map0!![map0.keySet().toArray().get(0)].concat("\n")
                .concat(map1!![map1.keySet().toArray().get(0)]).concat("\n").concat(
                    map2!![map2.keySet().toArray().get(0)]
                ).concat("\n").concat(map3!![map3.keySet().toArray().get(0)]).concat("\n").concat(
                    map4!![map4.keySet().toArray().get(0)]
                )
            //
        } else {
            for (m0 in map0.entrySet()) {
                for (m1 in map1.entrySet()) {
                    for (m2 in map2.entrySet()) {
                        for (m3 in map3.entrySet()) {
                            for (m4 in map4.entrySet()) {
                                sum =
                                    m0.getKey() + m1.getKey() + m2.getKey() + m3.getKey() + m4.getKey()
                                if (sum > max_sum && sum <= budget) {
                                    max_sum = sum
                                    out =
                                        map0!![m0.getKey()].concat("\n").concat(map1!![m1.getKey()])
                                            .concat("\n").concat(
                                                map2!![m2.getKey()]
                                            ).concat("\n").concat(map3!![m3.getKey()]).concat("\n")
                                            .concat(
                                                map4!![m4.getKey()]
                                            )
                                }
                            }
                        }
                    }
                }
                if (max_sum == budget) {
                    break
                }
            }
        }
        println(out)
    }
}