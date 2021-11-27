package fft

import chisel3._
import chisel3.util._
import chisel3.iotesters.PeekPokeTester
/*
class DatapathTester(dut: fftDatapath) extends PeekPokeTester(dut){
 // poke(dut.io.stage_count, 0.U)
 // for (i <- 0 to 7){
   // println("it: " + i.U)
    //println("out_even = " + peek(dut.io.out_even).toString)
    //println("out_odd = " + peek(dut.io.out_odd).toString)
 // }
}

object DatapathTester extends App {
  println("testing Datapath...")
  iotesters.Driver.execute(Array[String](), () => new fftDatapath()){
    c => new DatapathTester(c)
  }
}

*/
