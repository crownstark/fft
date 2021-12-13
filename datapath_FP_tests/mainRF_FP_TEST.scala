package fft

import chisel3._
import chisel3.util._
import chisel3.experimental._
import chisel3.iotesters.PeekPokeTester

class mainRF_FP_TEST(dut: mainRF_FP) extends PeekPokeTester(dut){
  //pokeFixedPoint(dut.io.from_ROM(0), 1.1)
  //println(peekFixedPoint(dut.io.to_evenOdd(0)).toString)
  poke(dut.io.load_en, 1.U)
  step(1)
  for (i <- 0 until 8){
    pokeFixedPoint(dut.io.from_ROM(i), i+1.1)
    pokeFixedPoint(dut.io.from_BF(i), i+10.1)
  }
  poke(dut.io.out_en, 1.U)
  
  poke(dut.io.sel, 1.U)
  step(1)
  println("SEL = 1: FROM BUTTERFLY")
  for (i <- 0 until 8){
    poke(dut.io.rdaddr, i.U)
    println("to_evenOdd: " + peekFixedPoint(dut.io.to_evenOdd).toString)
    step(1)
  }
  poke(dut.io.sel, 0.U)
  
  step(1)
  println("SEL = 0: FROM ROM")
  for (i <- 0 until 8){
    poke(dut.io.rdaddr, i.U)
    println("to_evenOdd: " + peekFixedPoint(dut.io.to_evenOdd).toString)
    step(1)
  }
}

object mainRF_FP_TEST extends App{
  println("Testing FP Main Reg File...")
  iotesters.Driver.execute(Array[String](), () => new mainRF_FP()){
    c => new mainRF_FP_TEST(c)
  }
}
