package com.miramir.mahfaze.data.remote

import com.miramir.mahfaze.data.model.Note

class NoteBatchQueryRequest(val inserts: List<Note>, val updates: List<Note>)