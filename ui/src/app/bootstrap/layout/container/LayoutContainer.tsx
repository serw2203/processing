import * as React from 'react';
import {Col, Container, Row} from "react-bootstrap";
import RoutesContainer from '../../route/RoutesContainer';
import HeaderContainer from '../entity/header/container/HeaderContainer';

const LayoutContainer: React.FC<{}> = () => {
    return <Container style={{'border': '1px solid purple'}} fluid={true}>
        <Row style={{'border': '1px solid red'}}>
            <HeaderContainer/>
        </Row>
        <Row>
            <Col xs={3}>2.1</Col>
            <Col style={{'border': '1px solid black'}}>
                <RoutesContainer/>
            </Col>
        </Row>
        <Row style={{'border': '1px solid red'}}>
            <Col>3</Col>
        </Row>
    </Container>;
};

export default LayoutContainer;
