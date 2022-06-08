import {connect} from 'react-redux';
import {push} from 'connected-react-router';
import {Header} from '../component/Header/Header';

export default connect (null, {push})(Header)
