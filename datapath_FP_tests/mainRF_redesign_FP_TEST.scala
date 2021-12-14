package fft

import chisel3._
import chisel3.util._
import chisel3.experimental._
import chisel3.iotesters.PeekPokeTester

class mainRF_redesign_FP_TEST(dut: mainRF_redesign_FP) extends PeekPokeTester(dut){
  poke(dut.io.load_en, 1.U)
  step(1)
  for(i <- 0 until 8){
    pokeFixedPoint(dut.io.from_ROM(i), i+0.0)
    pokeFixedPoint(dut.io.from_BF(i), i+10.0)
  }
  poke(dut.io.out_en, 1.U)
  
  poke(dut.io.sel, 1.U)
  step(1)
  println("SEL = 1: FROM BF")
  for(i <- 0 until 8){
    println("out(" + i.toString + "): " + peekFixedPoint(dut.io.out(i)).toString)
    step(1)
  }
  
  poke(dut.io.sel, 0.U)
  step(1)
  println("SEL = 1: FROM ROM")
  for(i <- 0 until 8){
    println("out(" + i.toString + "): " + peekFixedPoint(dut.io.out(i)).toString)
    step(1)
  }
}

object mainRF_redesign_FP_TEST extends App {
  println("Testing FP Main Reg REDESIGN...")
  iotesters.Driver.execute(Array[String](), () => new mainRF_redesign_FP()){
    c => new mainRF_redesign_FP_TEST(c)
  }
}
    
