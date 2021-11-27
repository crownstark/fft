package fft

import chisel3._
import chisel3.util._
import chisel3.iotesters.PeekPokeTester

class RF_4x32Tester(dut: RF_4x32) extends PeekPokeTester(dut){
  
  poke(dut.io.wr_en, 1.U)
  poke(dut.io.out_en, 1.U)
  for (i <- 0 until 4){
    poke(dut.io.in, i.U)
    poke(dut.io.wr_addr, i.U)
    println("out(" + i.toString + ")=" + peek(dut.io.out(i)).toString)
    step(1)
  }
  for (i <-0 until 4){
    poke(dut.io.rdaddr(i), i.U)
    println("out(" + i.toString + ")=" + peek(dut.io.out(i)).toString)
    step(1)
  }
    
 
}

object RF_4x32Tester extends App{
  println("Testing RF_4x32...")
  iotesters.Driver.execute(Array[String](), () => new RF_4x32()){
    c => new RF_4x32Tester(c)
  }
}
