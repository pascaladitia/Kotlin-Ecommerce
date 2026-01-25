package com.pascal.model.request

data class ReviewRatingRequest(val productId:String, val reviewText:String, val rating:Int)
