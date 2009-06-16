;; posts lines containing urls to a delicious account
(ns hiredman.clojurebot.delicious
  (:require [hiredman.clojurebot.core :as core]
            [hiredman.utilities :as util])
  (:import (java.net URLEncoder URL)))

(def url-reg #"[A-Za-z]+://[^  ^/]+\.[^  ^/]+[^ ]+")

(defn post
  "posts a url to the delicious account of [user pass]"
  [[user pass] url descr tag]
  (util/shell (str "fetch -o /dev/null https://" user ":" pass "@api.del.icio.us/v1/posts/add?url=" (URLEncoder/encode url) "&description=" (URLEncoder/encode descr) "&tags=" (URLEncoder/encode tag))))

(core/defresponder ::delicious 21
  (core/dfn (and (re-find url-reg (:message msg))
                 (:channel msg))) ;;
  (let [url (re-find url-reg (:message msg))
        desc (:message msg)
        tag (str (:sender msg) " " (:channel msg))]
    (post (:delicious bot) url desc tag)))
