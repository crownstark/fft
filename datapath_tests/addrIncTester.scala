package fft

import chisel3._
import chisel3.util._
import chisel3.iotesters.PeekPokeTester

class addrIncTester(dut: addrInc) extends PeekPokeTester(dut) {
	/*println("NO COUNT_ENABLE")
	poke(dut.io.count_en, 0.U)
	for (i <- 0 to 7){
		println("it: " + i.toString)
		println("mainReg_rdaddr: " + peek(dut.io.mainReg_rdaddr).toString)
		println("evenOdd_wraddr: " + peek(dut.io.evenOdd_wraddr).toString)
		println("evenOdd_wr_en: " + peek(dut.io.evenOdd_wr_en).toString)
		println("rdy_forBF: " + peek(dut.io.rdy_forBF).toString)
		step(1)
	}*/
	println("WITH COUNT_ENABLE")
	poke(dut.io.count_en, 1.U)
	for (i <- 0 to 7){
		println("it: " + i.toString)
		println("mainReg_rdaddr: " + peek(dut.io.mainReg_rdaddr).toString)
		println("evenOdd_wraddr: " + peek(dut.io.evenOdd_wraddr).toString)
		println("evenOdd_wr_en: " + peek(dut.io.evenOdd_wr_en).toString)
		println("rdy_forBF: " + peek(dut.io.rdy_forBF).toString)
		step(1)
	}
}

object addrIncTester extends App {
	println("Testing addrInc")
	iotesters.Driver.execute(Array[String](), () => new addrInc()) {
		c => new addrIncTester(c)
	}
}
