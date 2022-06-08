import * as React from 'react';
import {Breadcrumb} from 'react-bootstrap';


const Header: React.FC<any> = () => {
    return <Breadcrumb style={{'height': '3em', 'width': '100%'}}>
        <Breadcrumb.Item href="#">Home</Breadcrumb.Item>
        <Breadcrumb.Item href="https://getbootstrap.com/docs/4.0/components/breadcrumb/">
            Library
        </Breadcrumb.Item>
        <Breadcrumb.Item active>Data</Breadcrumb.Item>
    </Breadcrumb>;
};

export {Header};

