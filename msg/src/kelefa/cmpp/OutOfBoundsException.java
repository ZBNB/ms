package com.kelefa.cmpp;


public class OutOfBoundsException extends Exception
{

    public OutOfBoundsException()
    {
        details = "array length or data type is invalid";
    }

    String details;
}
