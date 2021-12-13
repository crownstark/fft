package fft

import chisel3._
import chisel3.util._
import chisel3.experimental._
import chisel3.iotesters.PeekPokeTester

class addrInc_FP_TEST(dut: addrInc_FP) extends PeekPokeTester(dut){
    poke(dut.io.count_start, 1.U)
    step(1)
    poke(dut.io.count_start, 0.U)
    for (i <- 0 until 9){
      println("it: " + i.toString)
      println("mainReg_rdaddr: " + peek(dut.io.mainReg_rdaddr).toString)
      println("evenOdd_wraddr: " + peek(dut.io.evenOdd_wraddr).toString)
      println("evenOdd_wren: " + peek(dut.io.evenOdd_wren).toString)
      println("count_done: " + peek(dut.io.count_done).toString)
      step(1)
    }
}

object addrInc_FP_TEST extends App{
  println("Testing FP addrInc...")
  iotesters.Driver.execute(Array[String](), () => new addrInc_FP()){
    c => new addrInc_FP_TEST(c)
  }
}
