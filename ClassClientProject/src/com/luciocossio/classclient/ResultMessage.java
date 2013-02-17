package com.luciocossio.classclient;

public class ResultMessage {
	
	private String message;
	private Boolean wasSuccessful;
	
	public ResultMessage(String message, Boolean wasSuccessful){
		this.message = message;
		this.wasSuccessful = wasSuccessful;
	}

	public Boolean getWasSuccessful() {
		return wasSuccessful;
	}

	public void setWasSuccessful(Boolean wasSuccessful) {
		this.wasSuccessful = wasSuccessful;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (other instanceof ResultMessage)
		{
			ResultMessage otherObject = (ResultMessage)other;
			if (this.wasSuccessful == otherObject.wasSuccessful && this.message.equals(otherObject.message) )
			{
				return true;
			}
		}

		return false;
	}
}
