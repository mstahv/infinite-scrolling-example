package com.mycompany.mavenproject22.views;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.Route;

/**
 * An example how to listen scroll events using JS and how to load more items
 * when reached the end.
 * <p>
 * If using in actual application, it is higly suggested to extract the JS
 * hack to a superclass, or use an add-on like Viritin (VScroller 
 */
@Route("")
public class InfiniteScrollerView extends Scroller {
    
    VerticalLayout content = new VerticalLayout();


    public InfiniteScrollerView() {
        
        /* Makes the scroller "full screen", otherwise scrollbars will appear
         * at document level. In many use cases Scroller is part of a larger
         * view consuming only a part of the whole viewport, then you could
         * also use a fixed height.
         */
        setHeightFull();

        for (int i = 0; i < 100; i++) {
            content.add(new Paragraph("Row" + i));
        }

        setContent(content);

        /*
         * Use JS even listener to detect when user has reached the end of the
         * viewport.
         */
        getElement().executeJs("""
        var self = this;
        this.addEventListener("scroll", function(e) {
            if(self.scrollTop + self.clientHeight == self.scrollHeight) {
                self.$server.loadMoreRows();
            }
        });
        """);
        
        alwaysShowScrollbar();

    }

    @ClientCallable
    public void loadMoreRows() {
        /* 
         * As an example, add 100 more rows to the content. In a real world
         * project, you'd do a backend query for more items and add related 
         * components. Note, that if your backend query would be slow, you
         * should display a progress bar for the user and do the data fetch
         * asynchronously (e.g. like Twitter does it).
         */
        int componentCount = content.getComponentCount();
        for (int i = componentCount; i < componentCount + 100; i++) {
            content.add(new Paragraph("new item, row id " + i));
        }
    }

    /**
     * A css hack to always show scrollbar, to make it easier to understand
     * what is happening on certain platforms that hide scrollbars...
     */
    private void alwaysShowScrollbar() {
        Element style = new Element("style");
        style.setText("""
            ::-webkit-scrollbar {
                -webkit-appearance: none;
                width: 10px;
            }

            ::-webkit-scrollbar-thumb {
                border-radius: 4px;
                background-color: rgba(0,0,0,.5);
                -webkit-box-shadow: 0 0 1px rgba(255,255,255,.5);
            }
                      """);
        
        UI.getCurrent().getElement().appendChild(style);
    }

}
