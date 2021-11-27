package fft

import chisel3._
import chisel3.util._
import chisel3.experimental._
import chisel3.iotesters.PeekPokeTester

class ButterflyTester(dut: Butterfly) extends PeekPokeTester(dut){
  println("Without out_en:")
  poke(dut.io.a, 10.U)
  poke(dut.io.b, 2.U)
  poke(dut.io.wn, 2.U)
  println("out0 = " + peek(dut.io.out0).toString + " --- out1 = " + peek(dut.io.out1).toString)
  
  poke(dut.io.out_en, 1.U)
  println("With out_en:")
  println("out0 = " + peek(dut.io.out0).toString + " --- out1 = " + peek(dut.io.out1).toString)
  
}

object ButterflyTester extends App{
  println("Testing Butterfly Block...")
  iotesters.Driver.execute(Array[String](), () => new Butterfly()){
    c => new ButterflyTester(c)
  }
}
  
