package com.martingago.blog_retrofit.interfaces

import com.martingago.blog_retrofit.model.comment.CommentResponseItem

interface OnCommentAddedListener {
    /**
     * Método que se llama cuando se añade un nuevo comentario o una respuesta
     * @param newComment El nuevo comentario añadido.
     * @param replyToId El ID del comentario al que responde, o `null` si es un comentario raíz.
     */
    fun onCommentAdded(newComment: CommentResponseItem, replyToId: Long?)
}