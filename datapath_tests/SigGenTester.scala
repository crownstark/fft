package fft

import chisel3._
import chisel3.util._
import chisel3.iotesters.PeekPokeTester

class SigGenTester(dut: SigGen) extends PeekPokeTester(dut){
  for(i <- 0 to 7){
    println("out" + i.toString + " = " + peek(dut.io.sig_out(i)).toString)
  }
  
}

object SigGenTester extends App {
  println("Testing SigGen...")
  iotesters.Driver.execute(Array[String](), () => new SigGen()) {
    c => new SigGenTester(c)
  }
}
