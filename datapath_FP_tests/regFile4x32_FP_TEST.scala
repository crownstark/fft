package fft

import chisel3._
import chisel3.util._
import chisel3.experimental._
import chisel3.iotesters.PeekPokeTester

class regFile4x32_FP_TEST(dut: regFile4x32_FP) extends PeekPokeTester(dut){
  poke(dut.io.wren, 1.U)
  for (i <- 0 until 4){
    pokeFixedPoint(dut.io.in, i+0.1)
    poke(dut.io.wraddr, i.U)
    step(1)
  }
  
  poke(dut.io.out_en, 1.U)
  for (i <-0 until 4){
    poke(dut.io.rdaddr(i), i.U)
    println("at out(" + i.toString + "): " + peekFixedPoint(dut.io.out(i)).toString)
    step(1)
  }
}

object regFile4x32_FP_TEST extends App{
  println("Testing FP 4x32 Reg File...")
  iotesters.Driver.execute(Array[String](), () => new regFile4x32_FP()){
    c => new regFile4x32_FP_TEST(c)
  }
}
