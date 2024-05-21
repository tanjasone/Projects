import { useState } from "react";
import { baseUrl } from "../environment";

export default function PromptModal({title, fields, fetchUrlPath, fetchMethod="POST", responseCallback}) {
    var [fieldValues, setFieldValues] = useState({});
    var [status, setStatus] = useState(null);
    const [activeStatusTimeout, setActiveStatusTimeout] = useState(null);

    function handleSubmit() {
        fetch(baseUrl + fetchUrlPath, {
            method: fetchMethod,
            mode: "cors",
            body: JSON.stringify(fieldValues)
        }).then(async res => {
            var data = await res.json();
            console.log("Successful submit from modal: " + title, data);
            displayStatus("Success", "SUCCESS");
            responseCallback(data);
        }).catch(rej => {
            console.error(rej);
            displayStatus("An error has occurred", "FAILURE");
        })
    }

    function displayStatus(message, type) {
        setStatus({message, type})
        if(activeStatusTimeout) clearTimeout(activeStatusTimeout);
        setActiveStatusTimeout(setTimeout(() => {
            setStatus(null);
            setActiveStatusTimeout(null)
        }, 5000))
    }

    return (
        <div className="modal-background">
            <div className="modal">
                <button className="btn" style={{float: "right"}} 
                onClick={()=>responseCallback("cancel")}>X</button>
                <h3>{title}</h3>
                <hr />
                {
                    fields.map(f => (
                    <div key={f.name}>
                        <p>{f.name}</p>
                        <input type={f.type} onChange={e => {
                            setFieldValues(state => {
                                state[f.name] = e.target.value;
                                return state;
                            })
                        }}></input>
                    </div>
                    ))
                }

                <hr />
                {status ?
                    <p style={{
                    display:"inline-block",
                    color: status.type === "FAILURE" ? "red" : (status.type === "SUCCESS" ? "green" : "")
                }}>{status.message}</p>
                : null}
                <button onClick={handleSubmit} style={{float: "right"}}>Submit</button>
            </div>

        </div>
    );
}