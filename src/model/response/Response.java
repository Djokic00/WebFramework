package model.response;

import model.request.Header;

public abstract class Response {
    protected Header header;

    public Response() {
        this.header = new Header();
    }

    public abstract String render();

    public Header getHeader() {
        return header;
    }
}
