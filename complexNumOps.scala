package fft

import chisel3._
import chisel3.experimental._

class ComplexNum extends Bundle {
	val real = FixedPoint(32.W, 16.BP)
	val imag = FixedPoint(32.W, 16.BP)
}

class ComplexIO extends Bundle {
    val comp1 = Input(new ComplexNum())
    val comp2 = Input(new ComplexNum())
    val result = Output(new ComplexNum())
}

class ComplexAddition extends Module {
    val io = IO(new ComplexIO)
    io.result.real := io.comp1.real + io.comp2.real
    io.result.imag := io.comp1.imag + io.comp2.imag
}

object ComplexAddition {
    def apply(comp1: ComplexNum, comp2: ComplexNum):ComplexNum = {
	val inst = Module(new ComplexAddition)
	inst.io.comp1 := comp1
	inst.io.comp2 := comp2
	inst.io.result
    }
}

class ComplexSubtract extends Module {
    val io = IO(new ComplexIO)
    io.result.real := io.comp1.real - io.comp2.real
    io.result.imag := io.comp1.imag - io.comp2.imag
}

object ComplexSubtract {
    def apply(comp1: ComplexNum, comp2: ComplexNum):ComplexNum = {
	val inst = Module(new ComplexSubtract)
	inst.io.comp1 := comp1
	inst.io.comp2 := comp2
	inst.io.result
    }
}

class ComplexMult extends Module{
    val io = IO(new ComplexIO)
    io.result.real := io.comp1.real*io.comp2.real - io.comp1.imag*io.comp2.imag
    io.result.imag := io.comp1.real*io.comp2.imag + io.comp1.imag*io.comp1.real
}

object ComplexMult {
    def apply(comp1: ComplexNum, comp2: ComplexNum):ComplexNum = {
	val inst = Module(new ComplexMult)
	inst.io.comp1 := comp1
	inst.io.comp2 := comp2
	inst.io.result
    }
}

class BFIO extends Bundle{
    val in1 = Input(new ComplexNum())
    val in2 = Input(new ComplexNum())
    val Wn = Input(new ComplexNum())
    val out1 = Output(new ComplexNum())
    val out2 = Output(new ComplexNum())
}

class BF extends Module {
    val io = IO(new BFIO())
    val add1 = ComplexAddition(io.in1, io.in2)
    val mult1 = ComplexMult(io.in2, io.Wn)
    io.out1 := ComplexAddition(mult1, io.in1)
    io.out2 := ComplexSubtract(io.in1, mult1)
}

object BF {
    def apply(in1: ComplexNum, in2: ComplexNum, Wn: ComplexNum): (ComplexNum, ComplexNum) = {
	val inst = Module(new BF)
	inst.io.in1 := in1
	inst.io.in2 := in2
	inst.io.Wn := Wn
	(inst.io.out1, inst.io.out2)
    }
}
